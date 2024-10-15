package com.min01.mss.misc;

import com.min01.mss.MinsSpellbooks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MSSTags
{
    public static final TagKey<Item> TROLL_FOCUS = ItemTags.create(new ResourceLocation(MinsSpellbooks.MODID, "troll_focus"));
}
