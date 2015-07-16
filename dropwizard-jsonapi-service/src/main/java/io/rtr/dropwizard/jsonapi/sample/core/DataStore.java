package io.rtr.dropwizard.jsonapi.sample.core;

import io.rtr.dropwizard.jsonapi.sample.models.Article;
import io.rtr.dropwizard.jsonapi.sample.models.Person;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

public class DataStore {
  private final Map<String, Article> articles = Maps.newHashMap();
  private final Map<String, Person> people = Maps.newHashMap();
  private final Map<String, List<Article>> authorArticles = Maps.newHashMap();
  private final Map<String, Person> articleAuthors = Maps.newHashMap();

  public DataStore() {
    addArticle("1", "My Amazing Article about JSON API", "Lorem ipsum dolor sit amet.");
    addArticle("2", "I, Fashionator", "Lorem ipsum dolor sit amet.");
    addArticle("3", "Dropwizard Awsomeness", "Lorem ipsum dolor sit amet.");
    addPerson("2", "John Hite", "Rent the Runway");
    addPerson("4", "Eric Weinstein", "Rent the Runway");
    addAuthor("2", "1");
    addAuthor("2", "3");
    addAuthor("4", "2");
  }

  public List<Article> getArticles() {
    return Lists.newArrayList(articles.values());
  }

  public List<Person> getPeople() {
    return Lists.newArrayList(people.values());
  }

  public Article getArticle(final String id) {
    return articles.get(id);
  }

  public Person getPerson(final String id) {
    return people.get(id);
  }

  public List<Article> getArticlesByAuthor(final String authorId) {
    return authorArticles.get(authorId);
  }

  public Person getAuthor(final String articleId) {
    return articleAuthors.get(articleId);
  }

  private void addAuthor(final String personId, final String articleId) {
    List<Article> articles = authorArticles.get(personId);
    if (articles == null) {
      articles = Lists.newArrayList();
      authorArticles.put(personId, articles);
    }
    articles.add(getArticle(articleId));

    articleAuthors.put(articleId, getPerson(personId));
  }

  public Person addPerson(final String id, final String name, final String company) {
    final Person p = createPerson(id, name, company);
    people.put(id, p);
    return p;
  }

  public void removePerson(final String id) {
    people.remove(id);
  }

  private void addArticle(final String id, final String title, final String body) {
    final Article a = createArticle(id, title, body);
    articles.put(id, a);
  }

  private static Article createArticle(final String id, final String title, final String body) {
    final Article article = new Article();
    article.setId(id);
    article.setTitle(title);
    article.setBody(body);
    return article;
  }

  private Person createPerson(final String id, final String name, final String company) {
    final Person person = new Person();
    person.setId(id);
    person.setName(name);
    person.setCompany(company);
    person.setCreated(DateTime.now().toString("yyyy/MM/dd"));
    return person;
  }
}
