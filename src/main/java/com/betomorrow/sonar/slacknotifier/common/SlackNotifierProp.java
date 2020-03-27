package com.betomorrow.sonar.slacknotifier.common;

import com.betomorrow.sonar.slacknotifier.SlackNotifierPlugin;

public enum SlackNotifierProp {

    /**
     * The Slack Incoming Web Hook URL
     */
    HOOK("beto.hook"),

    /**
     * Appear in Slack channels as this user
     */
    USER("beto.user"),

    /**
     * Appear in Slack channels with this icon
     */
    ICON_URL("beto.icon"),
    /**
     * Is this plugin enabled in general?
     * Per project slack notification sending depends on this and a project specific slack channel configuration existing.
     */
    ENABLED("beto.enabled"),
    
    /**
     * Proxy settings (IP, port, protocol)
     */
    PROXY_IP("beto.proxy_ip"),
    PROXY_PORT("beto.proxy_port"),
    PROXY_PROTOCOL("beto.proxy_protocol"),
    /**
     * Appear in Slack channels as this user
     */
    DEFAULT_CHANNEL("beto.channel"),
    /**
     * Include branch name in slack message (only supported in licenced versions of SonarQube)
     */
    INCLUDE_BRANCH("beto.include_branch"),

    /**
     * <p>
     * The project specific slack channels have to be configured in General, server side settings, instead of per project
     * This property is the prefix for a comma separated value list of Sonar Project Keys. For every project key there is a slack channel configuration.
     * This is a standard SonarQube way of configuring multivalued fields with org.sonar.api.config.PropertyDefinition.Builder#fields
     * </p>
     * <pre>
     *     beto.projectchannels=com.koant.sonar.slack:sonar-slack-notifier-plugin,some:otherproject
     *
     *     beto.projectchannels.com.koant.sonar.slack:sonar-slack-notifier-plugin.project=com.koant.sonar.slack:sonar-slack-notifier-plugin
     *     beto.projectchannels.com.koant.sonar.slack:sonar-slack-notifier-plugin.channel=#random
     *
     *     beto.projectchannels.some:otherproject.project=some:otherproject
     *     beto.projectchannels.some:otherproject.channel=#general
     * </pre>
     *
     * @see SlackNotifierPlugin#define(org.sonar.api.Plugin.Context)
     */
    CONFIG("beto.projectconfig"),

    /**
     * @see SlackNotifierProp#CONFIG
     */
    PROJECT_HOOK("projectHook"),
    /**
     * @see SlackNotifierProp#CONFIG
     */
    PROJECT_REGEXP("project"),

    /**
     * @see SlackNotifierProp#CONFIG
     */
    CHANNEL("channel"),

    /**
     * add @ to someone on notify message
     */
    NOTIFY("notify"),

    /**
     * @see SlackNotifierProp#CONFIG
     */
    QG_FAIL_ONLY("qg");

    private String property;

    SlackNotifierProp(java.lang.String property) {

        this.property = property;
    }

    public String property() {

        return property;
    }
}
