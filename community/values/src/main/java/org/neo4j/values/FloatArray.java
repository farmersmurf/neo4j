/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.values;

import java.util.Arrays;

import static java.lang.String.format;

abstract class FloatArray extends FloatingPointArray
{
    abstract float[] value();

    @Override
    public int length()
    {
        return value().length;
    }

    @Override
    public double doubleValue( int index )
    {
        return value()[index];
    }

    @Override
    public boolean equals( Object other )
    {
        return other != null && other instanceof Value && equals( (Value) other );
    }

    @Override
    public int hashCode()
    {
        return NumberValues.hash( value() );
    }

    @Override
    public boolean equals( Value other )
    {
        return other.equals( value() );
    }

    @Override
    public boolean equals( byte[] x )
    {
        return PrimitiveArrayValues.equals( x, value() );
    }

    @Override
    public boolean equals( short[] x )
    {
        return PrimitiveArrayValues.equals( x, value() );
    }

    @Override
    public boolean equals( int[] x )
    {
        return PrimitiveArrayValues.equals( x, value() );
    }

    @Override
    public boolean equals( long[] x )
    {
        return PrimitiveArrayValues.equals( x, value() );
    }

    @Override
    public boolean equals( float[] x )
    {
        return Arrays.equals( value(), x );
    }

    @Override
    public boolean equals( double[] x )
    {
        return PrimitiveArrayValues.equals( value(), x  );
    }

    @Override
    public void writeTo( ValueWriter writer )
    {
        PrimitiveArrayWriting.writeTo( writer, value() );
    }

    @Override
    public Object asPublic()
    {
        return value().clone();
    }

    static final class Direct extends FloatArray
    {
        final float[] value;

        Direct( float[] value )
        {
            assert value != null;
            this.value = value;
        }

        @Override
        float[] value()
        {
            return value;
        }

        @Override
        public String toString()
        {
            return format( "FloatArray%s", Arrays.toString( value() ) );
        }
    }

    static final class Lazy extends FloatArray implements LazyValue<float[]>
    {
        private volatile Object field;

        Lazy( Values.ValueLoader<float[]> producer )
        {
            this.field = producer;
        }

        @Override
        float[] value()
        {
            return LazyValues.getOrLoad( this );
        }

        @Override
        public void registerValue( float[] value )
        {
            this.field = value;
        }

        @Override
        public Object getMaybeValue()
        {
            return field;
        }

        @Override
        public String toString()
        {
            return format( "FloatArray%s",
                    LazyValues.valueIsLoaded( field ) ? Arrays.toString( value() ) : "?" );
        }
    }
}
