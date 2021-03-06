/*
 * (c) 2015 CenturyLink. All Rights Reserved.
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

package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.StaticCredentialsProvider;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.core.injector.SdkInjector;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import javax.ws.rs.client.ClientRequestContext;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

/**
 * @author ilya.drabenia
 */
@Test(groups = INTEGRATION)
public class ErrorProcessingFilterTest {

    @Test(expectedExceptions = ClcBadRequestException.class)
    public void testIncorrectLogin() throws Throwable {
        SdkInjector
            .createInjector(
                SdkConfiguration.DEFAULT.asModule(),
                new AuthModule(new StaticCredentialsProvider("12345", "456789"))
            )
            .getInstance(BearerAuthentication.class)
            .filter(Mockito.mock(ClientRequestContext.class));
    }

}
