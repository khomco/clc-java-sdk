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
package com.centurylink.cloud.sdk.policy.services.dsl.domain;

import java.util.Arrays;
import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
public class ActionSettingsEmail implements ActionSettings {
    private List<String> recipients;

    public ActionSettingsEmail(List<String> recipients) {
        recipients(recipients);
    }

    public ActionSettingsEmail(String... recipients) {
        recipients(recipients);
    }

    public ActionSettingsEmail recipients(List<String> recipients) {
        this.recipients = recipients;
        return this;
    }

    public ActionSettingsEmail recipients(String... recipients) {
        this.recipients = Arrays.asList(recipients);
        return this;
    }

    public List<String> getRecipients() {
        return recipients;
    }
}
