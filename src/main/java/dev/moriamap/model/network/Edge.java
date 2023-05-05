package dev.moriamap.model.network;

/**
 * A directed and weighted edge of graph. An Edge going from a Vertex to the same Vertex (according
 * to the equals method) is not allowed.
 */
public abstract class Edge {
  // The origin of this Edge
  private final Vertex from;

  // The destination of this Edge
  private final Vertex to;

  /**
   * Constructs an Edge going from Vertex "from" to Vertex "to".
   *
   * @param from the origin of this Edge
   * @param to the destination of this Edge
   */
  protected Edge(Vertex from, Vertex to) {
    if (from == null || to == null)
      throw new IllegalArgumentException("Null from or to is not allowed");
    if (from.equals(to)) throw new IllegalArgumentException("from.equals(to) true is not allowed");
    this.from = from;
    this.to = to;
  }

  /** {@return the origin Vertex of this Edge} */
  public Vertex getFrom() {
    return this.from;
  }

  /** {@return the destination Vertex of this Edge} */
  public Vertex getTo() {
    return this.to;
  }
}
