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

package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author Aliaksandr Krasitski
 */
public class NoWaitingJobFuture implements JobFuture {

    @Override
    public void waitUntilComplete() { }

    @Override
    public void waitUntilComplete(Duration timeout) { }

    @Override
    public CompletableFuture<Void> waitAsync() {
        return
            new CompletableFuture<Void>() {{
                complete(null);
            }};
    }

}
