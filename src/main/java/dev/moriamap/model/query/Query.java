package dev.moriamap.model.query;

import dev.moriamap.Logging;
import dev.moriamap.model.network.TransportNetwork;
import java.io.IOException;
import java.io.OutputStream;

/** A Query represents a request for route computation or schedule information fetching. */
public abstract class Query {

  /** Output stream where the result of the query will be written */
  private final OutputStream out;

  /**
   * Protected constructor for Query
   *
   * @param out the OutputStream where the result of the query will be written
   */
  protected Query(OutputStream out) {
    this.out = out;
  }

  /**
   * Executes this Query on the given network.
   *
   * @param network the network this Query acts on
   * @return The resulted String of the query
   * @throws QueryFailureException if an issue happened with the inputs
   */
  protected abstract String run(TransportNetwork network) throws QueryFailureException;

  /**
   * Call the method run() and print its result in the outputStream
   *
   * @param network the network that will be passed to the run() method
   * @throws IOException if an issue happened writing ou the OutputStream
   */
  private void writeResult(TransportNetwork network) throws IOException {
    String result;
    try {
      result = run(network) + "\n";
    } catch (QueryFailureException e) {
      result = "Error: " + e.getMessage() + "\n";
    }
    if (out != null) out.write(result.getBytes());
  }

  /**
   * Public method to execute the query and print any errors on OutputStream or the Logger
   *
   * @param network the network that will be used to run the query
   */
  public void execute(TransportNetwork network) {
    try {
      writeResult(network);
    } catch (IOException ex) {
      Logging.getLogger().severe("Failed to write query's output to OutputStream");
    }
  }
}
