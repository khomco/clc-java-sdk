/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.core.auth;

import com.centurylink.cloud.sdk.core.auth.client.LoginClient;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.CredentialsProvider;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * @author ilya.drabenia
 */
public class AuthModule extends AbstractModule {
    private final CredentialsProvider credentialsProvider;

    public AuthModule() {
        this.credentialsProvider = new PropertiesFileCredentialsProvider();
    }

    public AuthModule(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    protected void configure() {
        bind(LoginClient.class);
        bind(BearerAuthentication.class);
    }

    @Provides
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }
}
