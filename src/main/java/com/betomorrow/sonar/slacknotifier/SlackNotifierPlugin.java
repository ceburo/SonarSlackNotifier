package com.betomorrow.sonar.slacknotifier;

import com.betomorrow.sonar.slacknotifier.common.SlackNotifierProp;
import com.betomorrow.sonar.slacknotifier.extension.task.SlackPostProjectAnalysisTask;
import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyFieldDefinition;

import java.util.ArrayList;
import java.util.List;

public class SlackNotifierPlugin implements Plugin {

    private static final String CATEGORY = "Slack";
    private static final String SUBCATEGORY = "Slack Notifier";

    @Override
    public void define(Context context) {
        List<Object> extensions = new ArrayList<>();

        // The configurable properties
        addPluginPropertyDefinitions(extensions);

        // The actual plugin component(s)
        extensions.add(SlackPostProjectAnalysisTask.class);

        context.addExtensions(extensions);
    }

    private void addPluginPropertyDefinitions(List<Object> extensions) {
        extensions.add(PropertyDefinition.builder(SlackNotifierProp.HOOK.property())
            .name("Slack web integration hook")
            .description("https://api.slack.com/incoming-webhooks")
            .type(PropertyType.STRING)
            .category(CATEGORY)
            .subCategory(SUBCATEGORY)
            .index(0)
            .build());
        extensions.add(PropertyDefinition.builder(SlackNotifierProp.USER.property())
            .name("Slack user alias")
            .description("Messages from this plugin appear with given username")
            .defaultValue("SonarQube")
            .type(PropertyType.STRING)
            .category(CATEGORY)
            .subCategory(SUBCATEGORY)
            .index(1)
            .build());
        extensions.add(PropertyDefinition.builder(SlackNotifierProp.PROXY_PROTOCOL.property())
            .name("Proxy protocol")
            .description("Protocol to use to connect to proxy server")
            .defaultValue("HTTP")
            .type(PropertyType.SINGLE_SELECT_LIST)
            .options("DIRECT", "HTTP", "SOCKS")
            .category(CATEGORY)
            .subCategory(SUBCATEGORY)
            .index(2)
            .build());
        extensions.add(PropertyDefinition.builder(SlackNotifierProp.ENABLED.property())
            .name("Plugin enabled")
            .description("Enable this plugin ?")
            .defaultValue("false")
            .type(PropertyType.BOOLEAN)
            .category(CATEGORY)
            .subCategory(SUBCATEGORY)
            .index(3)
            .build());
        extensions.add(PropertyDefinition.builder(SlackNotifierProp.INCLUDE_BRANCH.property())
            .name("Branch enabled")
            .description("Include branch name in slack messages?\nNB: Not supported with free version of SonarQube")
            .defaultValue("false")
            .type(PropertyType.BOOLEAN)
            .category(CATEGORY)
            .subCategory(SUBCATEGORY)
            .index(4)
            .build());
        extensions.add(
            PropertyDefinition.builder(SlackNotifierProp.CONFIG.property())
                .name("Project specific configuration")
                .description("Project specific configuration: Specify Slack channel and notification only on failing Quality Gate. " +
                        "If a slack channel is not configured for a project, no slack message will be sent for project.")
                .category(CATEGORY)
                .subCategory(SUBCATEGORY)
                .index(5)
                .fields(
                    PropertyFieldDefinition.build(SlackNotifierProp.PROJECT_HOOK.property())
                        .name("Project Hook")
                        .description("https://api.slack.com/incoming-webhooks")
                        .type(PropertyType.STRING)
                        .build(),
                    PropertyFieldDefinition.build(SlackNotifierProp.PROJECT_REGEXP.property())
                        .name("Project Key")
                        .description("Ex: com.pole-emploi.recherche, can use '*' wildcard at the end")
                        .description("Regex that will match the Project Key of the project. Ex: com\\..* will match all projects that start with 'com.'")
                        .type(PropertyType.STRING)
                        .build(),
                    PropertyFieldDefinition.build(SlackNotifierProp.CHANNEL.property())
                        .name("Slack channel")
                        .description("Channel to send project specific messages to")
                        .type(PropertyType.STRING)
                        .build(),
                        PropertyFieldDefinition.build(SlackNotifierProp.QG_FAIL_ONLY.property())
                        .name("Send on failed Quality Gate")
                        .description("Should notification be sent only if Quality Gate did not pass OK")
                        .type(PropertyType.BOOLEAN)
                        .build()
                )
                .build());
    }
}
