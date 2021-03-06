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
package uk.q3c.krail.core.shiro;

import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.multibindings.OptionalBinder;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.guice.ShiroModule;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Bindings for Shiro and user related implementations
 *
 * @author David Sowerby 15 Jul 2013
 */
public class DefaultShiroModule extends ShiroModule {

    private List<Class<? extends Realm>> realms = new ArrayList<>();
    private OptionalBinder<CacheManager> cacheManagerBinder;
    private boolean cacheEnabled = false;

    public DefaultShiroModule() {
        super();
    }

    @Override
    protected void configureShiro() {

        cacheManagerBinder = OptionalBinder.newOptionalBinder(binder(), CacheManager.class);
        bindCredentialsMatcher();
        bindLoginAttemptLog();
        bindRealms();
        bindSubjectIdentifier();
        expose(SubjectIdentifier.class);
        bindSubjectProvider();
        expose(SubjectProvider.class);
        bindJWTKeyProvider();
        expose(JWTKeyProvider.class);
        bindJWTProvider();
        TypeLiteral<JWTProvider<KrailJWTBody>> providerInterface = new TypeLiteral<JWTProvider<KrailJWTBody>>() {
        };
        expose(providerInterface);

        if (cacheEnabled) {
            bindCacheManager();
        }

    }

    public DefaultShiroModule enableCache() {
        cacheEnabled = true;
        return this;
    }

    /**
     * Override this method to provide a different implementation of {@link CacheManager}. See http://shiro.apache.org/caching.html
     */
    protected void bindCacheManager() {
        cacheManagerBinder.setBinding()
                          .to(MemoryConstrainedCacheManager.class)
                          .in(Singleton.class);
    }

    private void bindRealms() {
        if (realms.isEmpty()) {
            realms.add(DefaultRealm.class);
        }
        for (Class<? extends Realm> realmClass : realms) {
            bindRealm().to(realmClass);
        }
    }

    /**
     * Override this to provide your own {@link SubjectIdentifier} implementation
     */
    protected void bindSubjectIdentifier() {
        bind(SubjectIdentifier.class).to(DefaultSubjectIdentifier.class);
    }

    /**
     * Override this to bind your own implementation of {@link LoginAttemptLog}
     */
    protected void bindLoginAttemptLog() {
        bind(LoginAttemptLog.class).to(DefaultLoginAttemptLog.class);
    }

    /**
     * Override this method to bind your own {@link CredentialsMatcher} implementation
     */
    protected void bindCredentialsMatcher() {
        bind(CredentialsMatcher.class).to(AlwaysPasswordCredentialsMatcher.class);
    }

    protected void bindSubjectProvider() {
        bind(SubjectProvider.class).to(DefaultSubjectProvider.class);
        //        bind(Subject.class).toProvider(SubjectProvider.class);
    }

    protected void bindJWTProvider() {
        TypeLiteral<JWTProvider<KrailJWTBody>> providerInterface = new TypeLiteral<JWTProvider<KrailJWTBody>>() {
        };
        TypeLiteral<DefaultJWTProvider> providerImplementation = new TypeLiteral<DefaultJWTProvider>() {

        };
        bind(providerInterface).to(providerImplementation);
    }

    protected void bindJWTKeyProvider() {
        bind(JWTKeyProvider.class).to(DefaultJWTKeyProvider.class);
    }

    /**
     * Call to add one or more {@link Realm}s.  Multiple calls may be made
     */
    public DefaultShiroModule addRealm(Class<? extends Realm> realm) {
        this.realms.add(realm);
        return this;
    }

    @Override
    protected void bindSecurityManager(AnnotatedBindingBuilder<? super SecurityManager> bind) {
        try {
            bind.toConstructor(KrailSecurityManager.class.getConstructor(Collection.class, Optional.class))
                .asEagerSingleton();
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("This really shouldn't happen.  Either something has changed in Shiro, " +
                    "" + "or there's a bug in ShiroModule.", e);
        }
    }

    @Override
    protected void bindSessionManager(AnnotatedBindingBuilder<SessionManager> bind) {
        bind.to(VaadinSessionManager.class)
            .asEagerSingleton();
    }

}
