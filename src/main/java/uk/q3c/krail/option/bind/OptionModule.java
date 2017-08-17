/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */

package uk.q3c.krail.option.bind;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import uk.q3c.krail.core.guice.vsscope.VaadinSessionScoped;
import uk.q3c.krail.core.option.DefaultOptionPopup;
import uk.q3c.krail.core.option.OptionPopup;
import uk.q3c.krail.option.Option;
import uk.q3c.krail.option.OptionPermissionVerifier;
import uk.q3c.krail.option.UserHierarchy;
import uk.q3c.krail.option.UserHierarchyDefault;
import uk.q3c.krail.option.hierarchy.DefaultOptionPermissionVerifier;
import uk.q3c.krail.option.hierarchy.DefaultUserHierarchy;
import uk.q3c.krail.option.option.DefaultOption;
import uk.q3c.krail.option.persist.*;
import uk.q3c.krail.option.persist.cache.DefaultOptionCache;
import uk.q3c.krail.option.persist.cache.DefaultOptionCacheProvider;
import uk.q3c.krail.option.persist.dao.DefaultOptionDao;
import uk.q3c.krail.option.persist.source.DefaultOptionSource;
import uk.q3c.util.data.DataConverter;
import uk.q3c.util.data.DefaultDataConverter;
import uk.q3c.util.guava.GuavaCacheConfiguration;

import java.lang.annotation.Annotation;

/**
 * Configures the use of {@link Option}
 * <p>
 * Created by David Sowerby on 16/11/14.
 */
public class OptionModule extends AbstractModule {


    private Class<? extends Annotation> activeSource;

    /**
     * Configures a {@link Binder} via the exposed methods.
     */
    @Override
    protected void configure() {
        bindOption();
        bindOptionDaoWrapper();
        bindOptionCacheConfiguration();
        bindOptionCache();
        bindOptionCacheProvider();
        bindOptionPopup();
        bindDefaultActiveSource();
        bindCurrentOptionSource();
        bindOptionElementConverter();
        bindPermissionVerifier();
        bindUserHierarchies();
    }

    /**
     * Bind you own {@link UserHierarchy} implementations, using binding annotations to identify them.  At least one
     * must marked as default by using the {@link UserHierarchyDefault} annotation.
     */
    protected void bindUserHierarchies() {
        bind(UserHierarchy.class).annotatedWith(UserHierarchyDefault.class)
                .to(DefaultUserHierarchy.class);
    }

    /**
     * Override this method to provide your own {@link OptionPermissionVerifier} implementation.
     */
    protected void bindPermissionVerifier() {
        bind(OptionPermissionVerifier.class).to(DefaultOptionPermissionVerifier.class);
    }

    /**
     * Override this method to provide your own {@link DataConverter} implementation.
     */
    protected void bindOptionElementConverter() {
        bind(DataConverter.class).to(DefaultDataConverter.class);
    }

    protected void bindDefaultActiveSource() {
        bind(OptionPersistenceHelper.annotationClassLiteral()).annotatedWith(ActiveOptionSourceDefault.class)
                                                                 .toInstance(activeSource);
    }

    /**
     * Provides a Singleton which identifies the currently selected source for {@link Option}.  Override to provide your own implementation of {@link
     * OptionSource}.
     */
    protected void bindCurrentOptionSource() {
        bind(OptionSource.class).to(DefaultOptionSource.class);
    }


    /**
     * Override this method to provide your own {@link OptionPopup} implementation
     */
    protected void bindOptionPopup() {
        bind(OptionPopup.class).to(DefaultOptionPopup.class);
    }

    /**
     * Override this method to provide your own {@link OptionCacheProvider} implementation.
     */
    protected void bindOptionCacheProvider() {
        bind(OptionCacheProvider.class).to(DefaultOptionCacheProvider.class);
    }


    /**
     * Override this method to provide your own {@link OptionCache} implementation. The scope is generally expected to be {@link VaadinSessionScoped}, as options
     * relate to individual users.
     */
    protected void bindOptionCache() {
        bind(OptionCache.class).to(DefaultOptionCache.class).in(VaadinSessionScoped.class);
    }

    protected void bindOptionCacheConfiguration() {
        bind(GuavaCacheConfiguration.class).annotatedWith(OptionCacheConfig.class)
                                           .toInstance(configureCache());
    }

    /**
     * Override this to configure the option cache
     *
     * @return a GuavaCacheConfiguration instance
     */
    protected GuavaCacheConfiguration configureCache() {
        GuavaCacheConfiguration config = new GuavaCacheConfiguration();
        config.maximumSize(5000)
              .recordStats();
        return config;
    }

    /**
     * Override this method to provide your own {@link Option} implementation.
     */
    protected void bindOption() {
        bind(Option.class).to(DefaultOption.class);
    }

    /**
     * Override this method to provide your own {@link OptionDao} implementation.
     */
    protected void bindOptionDaoWrapper() {
        bind(OptionDao.class).to(DefaultOptionDao.class);
    }




    /**
     * Defines which source should be used to supply {@link Option} values - the source is identified by its binding annotation {@code annotationClass}
     *
     * @param annotationClass the binding annotation which identifies the source
     * @return this for fluency
     */
    public OptionModule activeSource(Class<? extends Annotation> annotationClass) {
        activeSource = annotationClass;
        return this;
    }

}
