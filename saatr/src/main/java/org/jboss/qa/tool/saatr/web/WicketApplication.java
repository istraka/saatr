package org.jboss.qa.tool.saatr.web;

import java.net.URL;
import java.util.Properties;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.crypt.CharEncoding;
import org.jboss.qa.tool.saatr.entity.Build;
import org.jboss.qa.tool.saatr.util.IOUtils;
import org.jboss.qa.tool.saatr.web.comp.URLConverter;
import org.jboss.qa.tool.saatr.web.page.BuildPage;
import org.jboss.qa.tool.saatr.web.page.ConfigPage;
import org.jboss.qa.tool.saatr.web.page.DebugPage;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;

/**
 * Application object for the web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @author dsimko@redhat.com
 *
 */
@Component
public class WicketApplication extends WebApplication implements BeanFactoryPostProcessor {

    private String configFolderPath;
    private MongoClient mongoClient;

    @Override
    public Class<? extends Page> getHomePage() {
        return BuildPage.class;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerSingleton("datastore", createDatastore());
    }

    @Override
    protected void init() {
        super.init();

        mountPage("config", ConfigPage.class);
        mountPage("debug", DebugPage.class);

        getMarkupSettings().setStripWicketTags(true);
        getMarkupSettings().setDefaultMarkupEncoding(CharEncoding.UTF_8);

        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }

    private Datastore createDatastore() {
        Properties properties = IOUtils.loadFromClassPath("application.properties");
        configFolderPath = properties.getProperty("config.folder.path");
        mongoClient = new MongoClient(properties.getProperty("mongo.host"), Integer.parseInt(properties.getProperty("mongo.port")));
        final Morphia morphia = new Morphia();
        morphia.mapPackage(Build.class.getPackage().getName());
        morphia.getMapper().getOptions().setStoreEmpties(false);
        morphia.getMapper().getOptions().setStoreNulls(false);

        Datastore datastore = morphia.createDatastore(mongoClient, properties.getProperty("mongo.database.name"));
        datastore.ensureIndexes();
        return datastore;
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator defaultLocator = new ConverterLocator();
        defaultLocator.set(URL.class, new URLConverter());
        return defaultLocator;
    }

    @Override
    protected void onDestroy() {
        mongoClient.close();
    }

    public static WicketApplication get() {
        return (WicketApplication) WebApplication.get();
    }

    public String getConfigFolderPath() {
        return configFolderPath;
    }

    public void setConfigFolderPath(String configFolderPath) {
        this.configFolderPath = configFolderPath;
    }
}
