package io.rtr.jsonapi.filter;

import io.rtr.jsonapi.filter.mapping.ResourceMappingContext;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.jaxrs.cfg.EndpointConfigBase;
import com.fasterxml.jackson.jaxrs.cfg.ObjectWriterModifier;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class FilteredObjectWriterModifier extends ObjectWriterModifier {
	private static final Logger log = LoggerFactory.getLogger(FilteredObjectWriterModifier.class);
	private UriInfo uriInfo;
	private ResourceMappingContext mapping;
	
	public FilteredObjectWriterModifier(UriInfo info, ResourceMappingContext mapping) {
		this.uriInfo = info;
		this.mapping = mapping;
	}
	
	private Map<String, Set<String>> getFieldFilters() {
		final Map<String, Set<String>> filters = Maps.newHashMap();
		for (String param : uriInfo.getQueryParameters().keySet()) {
			if (param.startsWith("fields")) {
				final int start = param.indexOf('[');
				final int end = param.indexOf(']');
				final String modelType = param.substring(start+1, end);
				Set<String> fieldFilters = filters.get(modelType);
				if (fieldFilters == null) {
					fieldFilters = Sets.newHashSet();
					filters.put(modelType, fieldFilters);
				}
				for (String field : uriInfo.getQueryParameters().get(param)) {
					final String[] modelFields = field.split(",");
					fieldFilters.addAll(Sets.newHashSet(modelFields));
				}
			}
		}
		return filters;
	}
	
	@Override
	public ObjectWriter modify(EndpointConfigBase<?> endpoint,
			MultivaluedMap<String, Object> responseHeaders,
			Object valueToWrite, ObjectWriter w, JsonGenerator g)
			throws IOException {
		//Class<?> mixinClass = w.getConfig().findMixInClassFor(valueToWrite.getClass());
		//System.out.println("Found Mixin: " + mixinClass);
		
		FilterProvider filters = null;
		if (uriInfo != null) {
			final Map<String, Set<String>> modelFilters = getFieldFilters();
			log.debug("Parsed Filters: {}", modelFilters);
			if (modelFilters != null && !modelFilters.isEmpty()) {
				filters = new SimpleFilterProvider().addFilter("FieldFilter", new ApiPropertyFilter(mapping, modelFilters));
			}
		}
		//If there were no filtes - make sure we add a no-op field filter so Jackson doesn't complain
		if (filters == null) {
			filters = new SimpleFilterProvider().addFilter("FieldFilter", SimpleBeanPropertyFilter.serializeAllExcept(new String[]{}));  
		}

		return w.with(filters);
	}
	
}