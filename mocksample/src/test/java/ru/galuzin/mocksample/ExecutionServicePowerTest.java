package ru.galuzin.mocksample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SettingsHelper.class)
public class ExecutionServicePowerTest {
    @Spy
    SettingsHelper settingsHelper = new SettingsHelper();
    @InjectMocks
    ExecutionService executionService;

    @Test
    public void test() throws Exception {
        final SettingsCache cache = Mockito.mock(SettingsCache.class);
        PowerMockito.whenNew(SettingsCache.class).withAnyArguments().thenReturn(cache);
        settingsHelper.postConstruct();
        Mockito.when(cache.getString(
                    Mockito.eq(SettingsHelper.SOME_SETTING),
                    Mockito.anyString()))
                .thenReturn("power mock");
        executionService.execute();
    }
}
