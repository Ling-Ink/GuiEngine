package io.github.toberocat.guiengine.api;

import io.github.toberocat.guiengine.api.action.OpenGuiAction;
import io.github.toberocat.guiengine.api.commands.GuiCommands;
import io.github.toberocat.guiengine.api.components.GuiComponentBuilder;
import io.github.toberocat.guiengine.api.components.provided.embedded.EmbeddedGuiComponent;
import io.github.toberocat.guiengine.api.components.provided.embedded.EmbeddedGuiComponentBuilder;
import io.github.toberocat.guiengine.api.components.provided.head.HeadItemComponent;
import io.github.toberocat.guiengine.api.components.provided.head.HeadItemComponentBuilder;
import io.github.toberocat.guiengine.api.components.provided.item.SimpleItemComponent;
import io.github.toberocat.guiengine.api.components.provided.item.SimpleItemComponentBuilder;
import io.github.toberocat.guiengine.api.components.provided.paged.PagedComponent;
import io.github.toberocat.guiengine.api.components.provided.paged.PagedComponentBuilder;
import io.github.toberocat.guiengine.api.components.provided.toggle.ToggleItemComponent;
import io.github.toberocat.guiengine.api.components.provided.toggle.ToggleItemComponentBuilder;
import io.github.toberocat.guiengine.api.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.api.function.*;
import io.github.toberocat.guiengine.api.function.call.ActionFunction;
import io.github.toberocat.guiengine.api.function.call.AddComponentsFunction;
import io.github.toberocat.guiengine.api.function.call.EditComponentFunction;
import io.github.toberocat.guiengine.api.function.call.RemoveComponentFunction;
import io.github.toberocat.guiengine.api.function.compute.GuiComponentPropertyFunction;
import io.github.toberocat.guiengine.api.interpreter.InterpreterManager;
import io.github.toberocat.guiengine.api.interpreter.DefaultInterpreter;
import io.github.toberocat.guiengine.api.view.DefaultGuiViewManager;
import io.github.toberocat.toberocore.action.ActionCore;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiEngineApiPlugin extends JavaPlugin {
    private InterpreterManager interpreterManager;
    private DefaultGuiViewManager guiViewManager;

    @Override
    public void onEnable() {
        createManagers();
        registerListeners();

        registerInterpreters();
        registerComponents();
        registerFunctions();
        registerActions();

        addDefaultApi();
        registerCommands();
    }

    public static @NotNull GuiEngineApiPlugin getPlugin() {
        return getPlugin(GuiEngineApiPlugin.class);
    }

    public @NotNull InterpreterManager getInterpreterManager() {
        return interpreterManager;
    }

    public @NotNull DefaultGuiViewManager getGuiViewManager() {
        return guiViewManager;
    }

    private void createManagers() {
        guiViewManager = new DefaultGuiViewManager();
        interpreterManager = new InterpreterManager();
    }

    private void registerComponents() {
        GuiEngineApi.registerSharedFactory(
                SimpleItemComponent.TYPE,
                SimpleItemComponent.class,
                new SimpleItemComponentBuilder.Factory<>()
        );

        GuiEngineApi.registerSharedFactory(
                HeadItemComponent.TYPE,
                HeadItemComponent.class,
                new HeadItemComponentBuilder.Factory<>()
        );

        GuiEngineApi.registerSharedFactory(
                EmbeddedGuiComponent.TYPE,
                EmbeddedGuiComponent.class,
                new EmbeddedGuiComponentBuilder.Factory<>()
        );

        GuiEngineApi.registerSharedFactory(
                ToggleItemComponent.TYPE,
                ToggleItemComponent.class,
                new ToggleItemComponentBuilder.Factory()
        );

        GuiEngineApi.registerSharedFactory(
                PagedComponent.TYPE,
                PagedComponent.class,
                new PagedComponentBuilder.Factory()
        );
    }

    private void registerFunctions() {
        FunctionProcessor.registerFunction(AddComponentsFunction.ID, AddComponentsFunction.class);
        FunctionProcessor.registerFunction(EditComponentFunction.ID, EditComponentFunction.class);
        FunctionProcessor.registerFunction(RemoveComponentFunction.ID, RemoveComponentFunction.class);
        FunctionProcessor.registerFunction(ActionFunction.ID, ActionFunction.class);
        FunctionProcessor.registerComputeFunction(new GuiComponentPropertyFunction());
    }

    private void registerCommands() {
        new GuiCommands();
    }

    private void addDefaultApi() {
        GuiEngineApi guiApi = new GuiEngineApi("default", new File(getDataFolder(), "guis"));
        try {
            guiApi.reload();
        } catch (GuiIORuntimeException ignored) {

        }
    }

    private void registerActions() {
        ActionCore.register(new OpenGuiAction());
    }

    private void registerInterpreters() {
        interpreterManager.registerInterpreter(new DefaultInterpreter());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(guiViewManager, this);
    }
}