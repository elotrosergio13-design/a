package com.falsity.mod;

import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FalsityMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChatHandler {

    // clave de traduccion del chat -> posicion del argumento que lleva el nombre del jugador
    private static final Map<String, Integer> NAME_ARG = Map.of(
            "chat.type.text", 0,
            "chat.type.text.narrate", 0,
            "chat.type.emote", 0,
            "chat.type.announcement", 0,
            "commands.message.display.incoming", 0,
            "chat.type.team.text", 1
    );

    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent event) {
        Component msg = event.getMessage();
        if (!(msg.getContents() instanceof TranslatableContents tc)) return;

        Integer idx = NAME_ARG.get(tc.getKey());
        if (idx == null) return;

        Object[] args = tc.getArgs();
        if (args.length <= idx) return;

        Minecraft mc = Minecraft.getInstance();

        // Si el mensaje es tuyo, se deja tal cual.
        UUID sender = event.getSender();
        if (sender != null && sender.equals(mc.getUser().getProfileId())) return;

        Object nameArg = args[idx];
        String senderName = nameArg instanceof Component c ? c.getString() : String.valueOf(nameArg);
        if (senderName.equals(mc.getUser().getName())) return;

        Object[] copy = args.clone();
        copy[idx] = Component.literal(FalsityMod.FAKE_NAME);

        MutableComponent replaced = Component.translatable(tc.getKey(), copy).setStyle(msg.getStyle());
        msg.getSiblings().forEach(replaced::append);
        event.setMessage(replaced);
    }
}
