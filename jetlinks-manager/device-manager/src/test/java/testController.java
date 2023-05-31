import org.hswebframework.ezorm.rdb.mapping.ReactiveRepository;
import org.jetlinks.community.Interval;
import org.jetlinks.community.device.entity.DeviceTagEntity;
import org.jetlinks.community.device.service.DeviceConfigMetadataManager;
import org.jetlinks.community.device.service.LocalDeviceInstanceService;
import org.jetlinks.community.device.service.LocalDeviceProductService;
import org.jetlinks.community.device.service.data.DeviceDataService;
import org.jetlinks.community.device.web.DeviceInstanceController;
import org.jetlinks.community.device.web.request.AggRequest;
import org.jetlinks.community.io.excel.ImportExportService;
import org.jetlinks.community.relation.service.RelationService;
import org.jetlinks.community.timeseries.query.Aggregation;
import org.jetlinks.core.device.DeviceRegistry;
import org.jetlinks.core.message.codec.MessageDecodeContext;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class testController {


    public static final String deviceId = "7042926707040145006-1";
    @Test
    public void test() throws Exception{
        AggRequest aggRequest = new AggRequest();
        List<DeviceDataService.DevicePropertyAggregation> list = new ArrayList<>();
        DeviceDataService.DevicePropertyAggregation devicePropertyAggregation = new DeviceDataService.DevicePropertyAggregation();
        devicePropertyAggregation.setAgg(Aggregation.AVG);
        devicePropertyAggregation.setAlias("temp");
        devicePropertyAggregation.setProperty("temp");
        list.add(devicePropertyAggregation);
        aggRequest.setColumns(list);
        DeviceDataService.AggregationRequest aggregationRequest = new DeviceDataService.AggregationRequest();
        aggregationRequest.setFormat("yyyy-MM-dd HH:mm:ss");
        aggregationRequest.setFrom(new Date());
        aggregationRequest.setInterval(Interval.ofHours(1));
        Date to = new DateTime()
            .withHourOfDay(23)
            .withMinuteOfHour(59)
            .withSecondOfMinute(59)
            .toDate();
        aggregationRequest.setTo(to);
        aggRequest.setQuery(aggregationRequest);
        LocalDeviceInstanceService mock = Mockito.mock(LocalDeviceInstanceService.class);
        DeviceRegistry mock1 = Mockito.mock(DeviceRegistry.class);
        LocalDeviceProductService mock2 = Mockito.mock(LocalDeviceProductService.class);
        ImportExportService mock3 = Mockito.mock(ImportExportService.class);
        ReactiveRepository mock4 = Mockito.mock(ReactiveRepository.class);
        DeviceDataService mock5 = Mockito.mock(DeviceDataService.class);
        DeviceConfigMetadataManager mock6 = Mockito.mock(DeviceConfigMetadataManager.class);
        RelationService mock7 = Mockito.mock(RelationService.class);

        new DeviceInstanceController(mock, mock1, mock2, mock3, mock4, mock5, mock6, mock7)
            .aggDeviceProperty(deviceId, Mono.just(aggRequest)).subscribe();

    }
}
