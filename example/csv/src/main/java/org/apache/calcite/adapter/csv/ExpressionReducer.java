package org.apache.calcite.adapter.csv;

import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexExecutor;
import org.apache.calcite.rex.RexNode;

import java.util.List;

/**
 * @author tiny
 */
public class ExpressionReducer implements RexExecutor {
    @Override
    public void reduce(RexBuilder rexBuilder, List<RexNode> constExps, List<RexNode> reducedValues) {

    }
}
