package ru.pearx.libmc.common.blocks.properties;

import com.google.common.base.Optional;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Created by mrAppleXZ on 04.11.17 10:51.
 */
public class PropertyInt extends PropertyHelper<Integer>
{
    private final Set<Integer> allowedValues;

    protected PropertyInt(String name, int min, int max)
    {
        super(name, Integer.class);

        if (max < min)
        {
            throw new IllegalArgumentException("Max value of " + name + " must be >= than min (" + min + ")");
        }
        IntStream str = IntStream.range(min, max + 1);
        this.allowedValues = str.boxed().collect(Collectors.toSet());
    }

    @Override
    public Collection<Integer> getAllowedValues()
    {
        return allowedValues;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PropertyInt that = (PropertyInt) o;
        return Objects.equals(allowedValues, that.allowedValues);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), allowedValues);
    }

    public static PropertyInt create(String name, int min, int max)
    {
        return new PropertyInt(name, min, max);
    }

    @Override
    public Optional<Integer> parseValue(String value)
    {
        try
        {
            Integer integer = Integer.valueOf(value);
            return this.allowedValues.contains(integer) ? Optional.of(integer) : Optional.absent();
        }
        catch (NumberFormatException var3)
        {
            return Optional.absent();
        }
    }

    @Override
    public String getName(Integer value)
    {
        return value.toString();
    }
}
