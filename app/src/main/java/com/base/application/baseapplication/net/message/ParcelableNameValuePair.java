package com.base.application.baseapplication.net.message;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.LangUtils;

/**
 * NameValuePair implementation of {@link NameValuePair}.
 * <p>
 * 参考并优化 BasicNameValuePair.java
 *
 * @since 4.0
 */
public class ParcelableNameValuePair implements NameValuePair, Cloneable, Parcelable
{
	private final String name;
	private final String value;

	/**
	 * Default Constructor taking a name and a value. The value may be null.
	 *
	 * @param name  The name.
	 * @param value The value.
	 */
	public ParcelableNameValuePair(final String name,final String value)
	{
		super();
		if(name == null)
		{
			throw new IllegalArgumentException("Name may not be null");
		}
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return this.name;
	}

	public String getValue()
	{
		return this.value;
	}

	/**
	 * @param source
	 */
	private ParcelableNameValuePair(Parcel source)
	{
		name = source.readString();
		value = source.readString();
	}

	public static final Creator<ParcelableNameValuePair> CREATOR = new Creator<ParcelableNameValuePair>()
	{
		@Override
		public ParcelableNameValuePair createFromParcel(Parcel source)
		{
			return new ParcelableNameValuePair(source);
		}

		@Override
		public ParcelableNameValuePair[] newArray(int size)
		{
			return null;
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest,int flags)
	{
		dest.writeString(this.name);
		dest.writeString(this.value);
	}

	public String toString()
	{
		// don't call complex default formatting for a simple toString

		if(this.value == null)
		{
			return name;
		}
		else
		{
			int len = this.name.length() + 1 + this.value.length();
			CharArrayBuffer buffer = new CharArrayBuffer(len);
			buffer.append(this.name);
			buffer.append("=");
			buffer.append(this.value);
			return buffer.toString();
		}
	}

	public boolean equals(final Object object)
	{
		if(object == null)
		{
			return false;
		}
		if(this == object)
		{
			return true;
		}
		if(object instanceof NameValuePair)
		{
			BasicNameValuePair that = (BasicNameValuePair)object;
			return this.name.equals(that.getName()) && LangUtils.equals(this.value,that.getValue());
		}
		else
		{
			return false;
		}
	}

	public int hashCode()
	{
		int hash = LangUtils.HASH_SEED;
		hash = LangUtils.hashCode(hash,this.name);
		hash = LangUtils.hashCode(hash,this.value);
		return hash;
	}

	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

}
