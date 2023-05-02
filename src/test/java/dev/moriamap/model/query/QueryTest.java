package dev.moriamap.model.query;

import java.io.OutputStream;

import org.junit.jupiter.api.Test;

import dev.moriamap.model.network.TransportNetwork;

import static org.junit.jupiter.api.Assertions.*;

class QueryTest {

    private static class DummyQuery extends Query {
        String in;
        protected DummyQuery(OutputStream out, String in) {
            super(out);
            this.in = in;
        }
        @Override
        public String run(TransportNetwork network) throws QueryFailureException {
            if(in.equals("notAllowed"))
                throw new QueryFailureException("Input can't be 'notAllowed'");
            return "yes";
        }
    }

    @Test
    void dummyQueryNullOutputStreamTest() {
        Query query = new DummyQuery(null, "allowed");
        assertDoesNotThrow(
                () -> query.run(null)
        );
    }

    @Test
    void dummyQueryNotNullOutputStreamTest() {
        Query query = new DummyQuery(System.out, "allowed");
        assertDoesNotThrow(
                () -> query.execute(null)
        );
    }

    @Test
    void runThrowExceptionTest() {
        Query query = new DummyQuery(System.out, "notAllowed");
        assertThrows(
                QueryFailureException.class,
                () -> query.run(null)
        );
    }

}
