package org.apache.calcite.adapter.csv;

import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.rel.type.RelDataTypeSystem;

/**
 * @author tiny
 */
public class CsvTypeFactory extends JavaTypeFactoryImpl {

    public CsvTypeFactory(RelDataTypeSystem typeSystem) {
        super(typeSystem);
    }
}
