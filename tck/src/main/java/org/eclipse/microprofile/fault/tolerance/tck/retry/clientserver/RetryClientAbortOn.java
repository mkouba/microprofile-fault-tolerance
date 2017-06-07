/*
 *******************************************************************************
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.eclipse.microprofile.fault.tolerance.tck.retry.clientserver;

import java.io.IOException;
import java.sql.Connection;

import javax.enterprise.context.RequestScoped;

import org.eclipse.microprofile.fault.tolerance.inject.Retry;
/**
 * A client to demonstrate the abortOn conditions
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
@RequestScoped
public class RetryClientAbortOn {
    private int counterForInvokingConnenectionService;
    private int counterForInvokingWritingService;
    private int counterForInvokingServiceA;
    private int counterForInvokingServiceB;
    @Retry(abortOn = {IOException.class})
    public Connection serviceA() {
        counterForInvokingServiceA ++;
        return connectionService();
    }

    private Connection connectionService() {
        counterForInvokingConnenectionService++;
        throw new RuntimeException("Connection failed");
    }
    
    public int getRetryCountForConnectionService() {
        return counterForInvokingConnenectionService;
    }
    /**
     * The configured the max retries is 90 but the max duration is 100ms. 
     * Once the duration is reached, no more retries should be performed.
     */
    @Retry(abortOn = {RuntimeException.class})
    public void serviceB() {
        counterForInvokingServiceB ++;
        writingService();
    }

    private void writingService() {
        counterForInvokingWritingService ++;
        try {
            Thread.sleep(100);
            throw new RuntimeException("WritinService failed");
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
    
    public int getRetryCountForWritingService() {
        return counterForInvokingWritingService;
    }
    
    public int getRetryCounterForServiceA() {
        return counterForInvokingServiceA;
    }
    
    public int getRetryCounterForServiceB() {
        return counterForInvokingServiceB;
    }
}