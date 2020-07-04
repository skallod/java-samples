package ru.galuzin.mocksample

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import ru.galuzin.mocksample.kotlin.ExecutionService2
import ru.galuzin.mocksample.kotlin.SettingsCache2
import ru.galuzin.mocksample.kotlin.SettingsHelper2
import javax.sql.DataSource

@RunWith(PowerMockRunner::class)
@PrepareForTest(SettingsHelper2::class)
open class ExecutionService2KotlinTest {

    var datasource : DataSource = Mockito.mock(DataSource::class.java)

    @Spy
    var settingsHelper: SettingsHelper2 = SettingsHelper2(datasource)

    @InjectMocks
    lateinit var executionService: ExecutionService2

//    @Before
//    public void before(){
//        MockitoAnnotations.initMocks(this);
//    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun execute() {
//        Mockito.`when`(settingsHelper!!.someSetting).thenReturn("mocked")
//        executionService!!.execute()
        val cache = Mockito.mock(SettingsCache2::class.java)
        PowerMockito.whenNew(SettingsCache2::class.java).withAnyArguments().thenReturn(cache)
        settingsHelper.postConstruct()
        Mockito.`when`(cache.getString(Mockito.eq(SettingsHelper.SOME_SETTING), Mockito.anyString())).
                thenReturn("power mock")
        executionService.execute()
    }
}
