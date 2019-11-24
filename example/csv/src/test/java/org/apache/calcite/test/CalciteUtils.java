package org.apache.calcite.test;

import org.apache.calcite.adapter.csv.CsvSchema;
import org.apache.calcite.adapter.csv.CsvSchemaFactory;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.plan.Context;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.validate.SqlConformance;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;

import java.util.HashMap;
import java.util.List;

/**
 * register the users and jobs table
 *
 * @author matt
 * @date 2019-03-19 19:58
 */
public class CalciteUtils {

    public static SchemaPlus registerRootSchema() {
        SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        HashMap<String, Object> map = new HashMap<>();
        map.put("directory", "/Users/tiny/Applications/IdeaProjects/calcite/example/csv/src/test/resources/bug");
        Schema schema = CsvSchemaFactory.INSTANCE.create(rootSchema, "BUG", map);
        ((CsvSchema)schema).getTableMap();
        rootSchema.add("CSV", schema);
        return rootSchema;
    }

    public static SqlConformance conformance(FrameworkConfig config) {
        final Context context = config.getContext();
        if (context != null) {
            final CalciteConnectionConfig connectionConfig =
                    context.unwrap(CalciteConnectionConfig.class);
            if (connectionConfig != null) {
                return connectionConfig.conformance();
            }
        }
        return SqlConformanceEnum.DEFAULT;
    }

    public static RexBuilder createRexBuilder(RelDataTypeFactory typeFactory) {
        return new RexBuilder(typeFactory);
    }

    public static class ViewExpanderImpl implements RelOptTable.ViewExpander {
        public ViewExpanderImpl() {
        }

        @Override
        public RelRoot expandView(RelDataType rowType, String queryString, List<String> schemaPath,
                                  List<String> viewPath) {
            return null;
        }
    }
}
