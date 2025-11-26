package br.com.api.desafio.Enums;

import org.hibernate.usertype.UserType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;
import java.sql.*;

public class PostgreSQLEnumType implements UserType {

    private final Class<? extends Enum> enumClass;

    public PostgreSQLEnumType(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public int getSqlType() {
        return Types.OTHER; // ENUM no PostgreSQL
    }

    @Override
    public Class returnedClass() {
        return enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return x == y;
    }

    @Override
    public int hashCode(Object x) {
        return x.hashCode();
    }

    // Hibernate 6: nullSafeGet usa 'int position' em vez de 'String[] names'
    @Override
    public Object nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String name = rs.getString(position);
        return name == null ? null : Enum.valueOf(enumClass, name);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, ((Enum<?>) value).name(), Types.OTHER);
        }
    }

    @Override
    public Object deepCopy(Object value) { return value; }

    @Override
    public boolean isMutable() { return false; }

    @Override
    public Serializable disassemble(Object value) { return (Serializable) value; }

    @Override
    public Object assemble(Serializable cached, Object owner) { return cached; }

    @Override
    public Object replace(Object original, Object target, Object owner) { return original; }
}
