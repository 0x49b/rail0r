package org.thievent.rail0r.view.graph;

public interface IEdge extends IGraphNode {

    ICell getSource();

    ICell getTarget();

    boolean isDirected();
}