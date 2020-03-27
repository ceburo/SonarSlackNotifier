package com.betomorrow.sonar.slacknotifier.extension.task;

import com.betomorrow.sonar.slacknotifier.common.SlackNotifierProp;
import com.github.seratch.jslack.api.webhook.Payload;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.config.Settings;
import org.sonar.api.config.internal.ConfigurationBridge;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.i18n.I18n;

import java.io.IOException;
import java.util.Locale;

import static com.betomorrow.sonar.slacknotifier.extension.task.Analyses.PROJECT_KEY;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by 616286 on 3.6.2016.
 * Modified by poznachowski
 */
public class SlackPostProjectAnalysisTaskTest {

    private static final String HOOK          = "http://hook";
    private static final String DIFFERENT_KEY = "different:key";

    private CaptorPostProjectAnalysisTask postProjectAnalysisTask;

    private SlackPostProjectAnalysisTask task;

    private SlackHttpClient httpClient;

    private Settings settings;
    private I18n     i18n;

    @Before
    public void before() throws IOException {
        postProjectAnalysisTask = new CaptorPostProjectAnalysisTask();
        settings = new MapSettings();
        settings.setProperty(SlackNotifierProp.ENABLED.property(), "true");
        settings.setProperty(SlackNotifierProp.HOOK.property(), HOOK);
        settings.setProperty(SlackNotifierProp.CHANNEL.property(), "channel");
        settings.setProperty(SlackNotifierProp.USER.property(), "user");
        settings.setProperty(SlackNotifierProp.ICON_URL.property(), "");
        settings.setProperty(SlackNotifierProp.PROXY_IP.property(), "127.0.0.1");
        settings.setProperty(SlackNotifierProp.PROXY_PORT.property(), "8080");
        settings.setProperty(SlackNotifierProp.PROXY_PROTOCOL.property(), "http");
        settings.setProperty(SlackNotifierProp.DEFAULT_CHANNEL.property(), "general");
        settings.setProperty(SlackNotifierProp.CONFIG.property(), PROJECT_KEY);
        settings.setProperty(SlackNotifierProp.CONFIG.property() + "." + PROJECT_KEY + "." + SlackNotifierProp.PROJECT_REGEXP.property(), PROJECT_KEY);
        settings.setProperty(SlackNotifierProp.CONFIG.property() + "." + PROJECT_KEY + "." + SlackNotifierProp.CHANNEL.property(), "#random");
        settings.setProperty(SlackNotifierProp.CONFIG.property() + "." + PROJECT_KEY + "." + SlackNotifierProp.QG_FAIL_ONLY.property(), "false");
        settings.setProperty("sonar.core.serverBaseURL", "http://your.sonar.com/");
        httpClient = mock(SlackHttpClient.class);
        i18n = mock(I18n.class);
        when(i18n.message(Matchers.any(Locale.class), anyString(), anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[2];
            }
        });

        task = new SlackPostProjectAnalysisTask(httpClient, new ConfigurationBridge(settings), i18n);
    }

    @Test
    public void shouldCall() throws Exception {
        Analyses.simple(postProjectAnalysisTask);
        when(httpClient.invokeSlackIncomingWebhook(Mockito.eq(HOOK), isA(Payload.class))).thenReturn(true);
        task.finished(postProjectAnalysisTask.getProjectAnalysis());
        Mockito.verify(httpClient, times(1)).invokeSlackIncomingWebhook(Mockito.eq(HOOK), isA(Payload.class));
    }

    @Test
    public void shouldSkipIfPluginDisabled() throws Exception {
        settings.setProperty(SlackNotifierProp.ENABLED.property(), "false");
        Analyses.simple(postProjectAnalysisTask);
        task.finished(postProjectAnalysisTask.getProjectAnalysis());
        Mockito.verifyZeroInteractions(httpClient);
    }

    @Test
    public void shouldSkipIfReportFailedQualityGateButOk() throws Exception {
        settings.setProperty(SlackNotifierProp.CONFIG.property() + "." + PROJECT_KEY + "." + SlackNotifierProp.QG_FAIL_ONLY.property(), "true");
        Analyses.simple(postProjectAnalysisTask);
        task.finished(postProjectAnalysisTask.getProjectAnalysis());
        Mockito.verifyZeroInteractions(httpClient);
    }
}
