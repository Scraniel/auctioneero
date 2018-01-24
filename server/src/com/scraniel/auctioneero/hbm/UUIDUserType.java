package com.scraniel.auctioneero.hbm;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

//TODO: Extract into immutable type baseclass (eg. everything but get / set)
public class UUIDUserType implements UserType 
{
	@Override
	public boolean isMutable() 
	{
	    return false;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException 
	{
		if(x == null)
		{
			return x == y;
		}

	    return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException 
	{
	    assert (x != null);
	    return x.hashCode();
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException 
	{
	    return value;
	}

	@Override
	public Object replace(Object original, Object target, Object owner)
	        throws HibernateException 
	{
	    return original;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException 
	{
	    return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner)
	        throws HibernateException 
	{
	    return cached;
	}

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) 
    		throws SQLException 
    {
        String value = rs.getString(names[0]);
        return (value != null && value.length() > 0) ? UUID.fromString(value) : null;
    }
 
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) 
    		throws SQLException 
    {
        if (value == null) 
        {
            st.setNull(index, Types.CHAR);
        } 
        else 
        {
            st.setString(index, value.toString());
        }
    }

	@Override
	public Class<UUID> returnedClass() 
	{
		return UUID.class;
	}

	@Override
	public int[] sqlTypes() 
	{
		return new int[] { Types.VARCHAR };
	}

}
