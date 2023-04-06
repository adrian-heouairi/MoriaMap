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
	Stop terminus = Stop.from( "stop2", GeographicPosition.at( 34, 11 ) );
	Variant variant = Variant.empty( "Variant 2", "14" );

	@Test void constructorPassingNullTimeTest() {
		assertThrows(
				  NullPointerException.class,
				  () -> new TransportSchedule( null, stop, terminus, variant )
					);
	}

	@Test void constructorPassingNullStopTest() {
		assertThrows(
				  NullPointerException.class,
				  () -> new TransportSchedule( time, null, terminus, variant )
					);
	}

	@Test void constructorPassingNullTerminusTest() {
		assertThrows(
				  NullPointerException.class,
				  () -> new TransportSchedule( time, stop, null, variant )
					);
	}

	@Test void constructorPassingNullVariantTest() {
		assertThrows(
				  NullPointerException.class,
				  () -> new TransportSchedule( time, stop, terminus, null )
					);
	}

	@Test void getterTimeTest() {
		TransportSchedule schedule = new TransportSchedule( time, stop, terminus, variant );
		assertEquals(time, schedule.time());
	}

	@Test void getterStopTest() {
		TransportSchedule schedule = new TransportSchedule( time, stop, terminus, variant );
		assertEquals(stop, schedule.stop());
	}

	@Test void getterTerminusTest() {
		TransportSchedule schedule = new TransportSchedule( time, stop, terminus, variant );
		assertEquals(terminus, schedule.terminus());
	}

	@Test void getterVariantTest() {
		TransportSchedule schedule = new TransportSchedule( time, stop, terminus, variant );
		assertEquals(variant, schedule.variant());
	}

}
