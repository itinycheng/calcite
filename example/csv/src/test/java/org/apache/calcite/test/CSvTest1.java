package org.apache.calcite.test;

import org.apache.calcite.adapter.csv.CsvCostFactory;
import org.apache.calcite.adapter.csv.CsvFunctionOperatorTable;
import org.apache.calcite.adapter.csv.CsvSqlValidator;
import org.apache.calcite.adapter.csv.CsvTypeFactory;
import org.apache.calcite.adapter.csv.CsvTypeSystem;
import org.apache.calcite.adapter.csv.ExpressionReducer;
import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.plan.Contexts;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.RelCollationTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.apache.calcite.sql.util.ChainedSqlOperatorTable;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CSvTest1 {

    private static FrameworkConfig frameworkConfig;

    private CsvSqlValidator validator;

    private SqlToRelConverter sqlToRelConverter;

    private static final CsvTypeSystem typeSystem = new CsvTypeSystem();

    private static final CsvTypeFactory typeFactory = new CsvTypeFactory(typeSystem);

    @Test(expected = RuntimeException.class)
    public void testSubmitWrong() {
        String sql = "select * from (select id from test1) t1, test2 t2 where t1.id = t2.id order by id desc";

        final FrameworkConfig config = Frameworks.newConfigBuilder()
                .defaultSchema(CalciteSchema.createRootSchema(true).plus())
                .parserConfig(getSqlParserConfig())
                .costFactory(new CsvCostFactory())
                .typeSystem(new CsvTypeSystem())
                .sqlToRelConverterConfig(getSqlToRelConverterConfig())
                .operatorTable(getOperatorTable())
                .executor(new ExpressionReducer())
                .context(Contexts.empty())
                .traitDefs(getTraitDefs())
                .build();

        try {
            SqlParser parser = SqlParser.create(sql, config.getParserConfig());
            SqlNode sqlNode = parser.parseStmt();
            sqlNode = validate(sqlNode);

            sqlToRelConverter = new SqlToRelConverter(null, validator,
                    createCatalogReader(false),
                    null,
                    null,
                    config.getSqlToRelConverterConfig());
            RelRoot relRoot = sqlToRelConverter.convertQuery(sqlNode, false, true);
            // optimize
            String res = toQuery(relRoot.rel);
            System.out.println(sqlNode.toString());
            System.out.println(res);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String toQuery(RelNode rel) {
        return rel.toString();
    }

    private List<RelTraitDef> getTraitDefs() {
        return Arrays.asList(ConventionTraitDef.INSTANCE, RelCollationTraitDef.INSTANCE);
    }

    private SqlParser.Config getSqlParserConfig() {
        return SqlParser.configBuilder()
                .setParserFactory(SqlParserImpl.FACTORY)
                .setCaseSensitive(false)
                .setQuoting(Quoting.BACK_TICK)
                .setQuotedCasing(Casing.TO_UPPER)
                .setUnquotedCasing(Casing.TO_UPPER)
                .setConformance(SqlConformanceEnum.DEFAULT)
                .build();
    }

    private SqlOperatorTable getOperatorTable() {
        return ChainedSqlOperatorTable.of(SqlStdOperatorTable.instance(), new CsvFunctionOperatorTable());
    }

    private SqlToRelConverter.Config getSqlToRelConverterConfig() {
        return SqlToRelConverter.configBuilder()
                .withTrimUnusedFields(false)
                .withConvertTableAccess(false)
                .withInSubQueryThreshold(Integer.MAX_VALUE)
                .withExpand(false)
                .build();
    }

    private SqlNode validate(SqlNode sqlNode) {
        validator = new CsvSqlValidator(
                frameworkConfig.getOperatorTable(),
                createCatalogReader(false),
                typeFactory,
                getSqlParserConfig().conformance());
        validator.setIdentifierExpansion(true);
        return validator.validate(sqlNode);
    }

    private static CalciteCatalogReader createCatalogReader(boolean caseSensitive) {
        return null;/*new CalciteCatalogReader(
                CalciteSchema.from(rootSchema),
                CalciteSchema.from(defaultSchema).path(null),
                typeFactory,
                CalciteConfig.connectionConfig(parserConfig)*/
    }
}
