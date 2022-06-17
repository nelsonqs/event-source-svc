package com.event.source.svc.db.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonBinaryType implements UserType {

	@Override
	public int[] sqlTypes() {
		return new int[] {Types.JAVA_OBJECT};
	}

	@Override
	public Class<Object> returnedClass() {
		return Object.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		
		 final String cellContent = rs.getString(names[0]);
		    if (cellContent == null) {
		        return null;
		    }
		    try {
		        final ObjectMapper mapper = new ObjectMapper();
		        return mapper.readValue(cellContent.getBytes("UTF-8"), returnedClass());
		    } catch (final Exception exp) {
		        throw new RuntimeException("Failed to convert json type: " + exp.getMessage(), exp);
		    }
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		
		if (value == null) {
	        st.setNull(index, Types.OTHER);
	        return;
	    }
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final StringWriter writer = new StringWriter();
	        mapper.writeValue(writer, value);
	        writer.flush();
	        st.setObject(index, writer.toString(), Types.OTHER);
	    } catch (final Exception exp) {
	        throw new RuntimeException("Failed to convert json type: " + exp.getMessage(), exp);
	    }

	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		 try {
		        // use serialization to create a deep copy
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(bos);
		        oos.writeObject(value);
		        oos.flush();
		        oos.close();
		        bos.close();
		         
		        ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
		        Object obj = new ObjectInputStream(bais).readObject();
		        bais.close();
		        return obj;
		    } catch (ClassNotFoundException | IOException exp) {
		        throw new HibernateException(exp);
		    }
	}

	@Override
	public boolean isMutable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

}
