
package org.jboss.qa.tool.saatr.repo.build;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.jboss.qa.tool.saatr.domain.DocumentWithProperties;
import org.jboss.qa.tool.saatr.domain.build.Build;
import org.jboss.qa.tool.saatr.domain.build.BuildFilter;
import org.jboss.qa.tool.saatr.domain.build.BuildProperty;
import org.jboss.qa.tool.saatr.domain.build.TestCase;
import org.jboss.qa.tool.saatr.domain.build.TestSuite;
import org.jboss.qa.tool.saatr.jaxb.surefire.Testsuite;

/**
 * The interface for repository functionality that will be implemented manually.
 * 
 * @author dsimko@redhat.com
 */
interface BuildRepositoryCustom {

    Iterator<Build> query(long first, long count, BuildFilter filter);

    long count(BuildFilter filter);

    void fillBuildByTestsuites(List<Testsuite> input, Build build);

    void addIfAbsent(BuildProperty property, Set<BuildProperty> properties);

    <T extends DocumentWithProperties<?>> void addOrUpdateProperties(T documentWithProperties, Set<BuildProperty> properties);

    Iterable<String> findDistinctVariableNames();

    Iterable<String> findDistinctVariableValues(String name);

    Iterable<String> findDistinctSystemPropertiesNames();

    Iterable<String> findDistinctSystemPropertiesValues(String name);

    Iterable<String> findDistinctPropertiesNames();

    Iterable<String> findDistinctPropertiesValues(String name);

    TestSuite findTestsuiteById(UUID id);

    TestCase findTestcaseById(UUID id, int index);

    String aggregate(String query);

    List<Build> findFailedWithoutAdditionalInfo();

    void addConsoleText(Build buildDocument, String response);

    Iterator<Build> getRoots(BuildFilter filter);

    Iterator<? extends Build> getChildren(Build parent, BuildFilter filter);

    ObjectId findSimilar(String errorMessage);
}
