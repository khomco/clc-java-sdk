package com.centurylinkcloud.servers.config;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.service.*;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class ServersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerClient.class);

        bind(ServerService.class);
        bind(GroupConverter.class);
        bind(GroupService.class);
        bind(TemplateConverter.class);
        bind(TemplateService.class);
    }

}
