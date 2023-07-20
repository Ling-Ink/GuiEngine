package io.github.toberocat.guiengine.components.provided.item;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.components.AbstractGuiComponent;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.GeneratorContext;
import io.github.toberocat.guiengine.utils.JsonUtils;
import io.github.toberocat.guiengine.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class SimpleItemComponent extends AbstractGuiComponent {
    public static final @NotNull String TYPE = "item";
    protected final ItemStack stack;

    public SimpleItemComponent(int offsetX,
                               int offsetY,
                               @NotNull RenderPriority priority,
                               @NotNull String id,
                               @NotNull List<GuiFunction> clickFunctions,
                               @NotNull List<GuiFunction> dragFunctions,
                               @NotNull List<GuiFunction> closeFunctions,
                               @NotNull ItemStack stack,
                               boolean hidden) {
        super(offsetX, offsetY, 1, 1, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden);
        this.stack = stack;
    }


    @Override
    public void serialize(@NotNull GeneratorContext gen, @NotNull SerializerProvider serializers) throws IOException {
        super.serialize(gen, serializers);
        gen.writeStringField("material", stack.getType().name());

        ItemMeta meta = stack.getItemMeta();
        if (meta == null)
            return;

        gen.writeStringField("name", meta.getDisplayName());
        if (meta.getLore() != null)
            JsonUtils.writeArray(gen, "lore", meta.getLore().toArray());

        if (!(meta instanceof SkullMeta skullMeta))
            return;

        if (skullMeta.getOwningPlayer() == null)
            return;

        gen.writeStringField("head-owner", skullMeta.getOwningPlayer().getUniqueId()
                .toString());
    }

    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public void render(@NotNull Player viewer,
                       @NotNull ItemStack[][] inventory) {
        if (context == null || api == null)
            return;
        inventory[offsetY][offsetX] = stack;
    }
}
