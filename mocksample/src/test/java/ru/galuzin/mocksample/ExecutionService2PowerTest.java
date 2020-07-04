package ru.galuzin.mocksample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.galuzin.mocksample.kotlin.ExecutionService2;
import ru.galuzin.mocksample.kotlin.SettingsCache2;
import ru.galuzin.mocksample.kotlin.SettingsHelper2;

import javax.sql.DataSource;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SettingsHelper2.class)
public class ExecutionService2PowerTest {

    @Mock
    SettingsHelper2 settingsHelper;
    @InjectMocks
    ExecutionService2 executionService;

    @Test
    public void test() throws Exception {
        //final SettingsCache2 cache = Mockito.mock(SettingsCache2.class);
        //PowerMockito.whenNew(SettingsHelper2.class).withAnyArguments().thenReturn(settingsHelper);
        //settingsHelper.postConstruct();
        //Mockito.when(cache.getString(Mockito.eq(SettingsHelper.SOME_SETTING), Mockito.anyString())).thenReturn("power mock");
        Mockito.when(settingsHelper.getSomeSetting()).thenReturn("mocked power");
        executionService.execute();
    }
}
