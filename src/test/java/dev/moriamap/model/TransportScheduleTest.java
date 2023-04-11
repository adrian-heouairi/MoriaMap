package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.time.LocalTime;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class TransportScheduleTest {

	LocalTime time = LocalTime.of( 4,20,4);
	Stop stop = Stop.from( "stop1", GeographicPosition.at( 12, 21 ) );
	Variant variant = Variant.empty( "Variant 2", "14" );

	@Test void constructorPassingNullTimeTest() {
		assertThrows(
				  IllegalArgumentException.class,
				  () -> new TransportSchedule( null, stop, variant )
					);
	}

	@Test void constructorPassingNullStopTest() {
		assertThrows(
				  IllegalArgumentException.class,
				  () -> new TransportSchedule( time, null, variant )
					);
	}

	@Test void constructorPassingNullVariantTest() {
		assertThrows(
				  IllegalArgumentException.class,
				  () -> new TransportSchedule( time, stop, null )
					);
	}

	@Test void getterTimeTest() {
		TransportSchedule schedule = new TransportSchedule( time, stop, variant );
		assertEquals(time, schedule.time());
	}

	@Test void getterStopTest() {
		TransportSchedule schedule = new TransportSchedule( time, stop, variant );
		assertEquals(stop, schedule.stop());
	}

	@Test void getterVariantTest() {
		TransportSchedule schedule = new TransportSchedule( time, stop, variant );
		assertEquals(variant, schedule.variant());
	}

}
