package org.apache.calcite.adapter.csv;

import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.validate.SqlConformance;
import org.apache.calcite.sql.validate.SqlValidatorCatalogReader;
import org.apache.calcite.sql.validate.SqlValidatorImpl;

/**
 * @author tiny
 */
public class CsvSqlValidator extends SqlValidatorImpl {

    /**
     * Creates a validator.
     *
     * @param opTab         Operator table
     * @param catalogReader Catalog reader
     * @param typeFactory   Type factory
     * @param conformance   Compatibility mode
     */
    public CsvSqlValidator(SqlOperatorTable opTab, SqlValidatorCatalogReader catalogReader, RelDataTypeFactory typeFactory, SqlConformance conformance) {
        super(opTab, catalogReader, typeFactory, conformance);
    }
}
