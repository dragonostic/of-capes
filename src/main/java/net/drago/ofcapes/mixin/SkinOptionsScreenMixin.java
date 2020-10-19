package net.drago.ofcapes.mixin;

import java.math.BigInteger;
import java.util.Random;

import com.mojang.authlib.exceptions.AuthenticationException;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.screen.options.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

@Mixin(SkinOptionsScreen.class)
public abstract class SkinOptionsScreenMixin extends GameOptionsScreen {

    protected SkinOptionsScreenMixin(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, new TranslatableText("options.skinCustomisation.title"));
    }


    //@Inject(method = "init()V", at = @At("TAIL"))
    @Override
    public void init() {
        int i = 0;
        PlayerModelPart[] var2 = PlayerModelPart.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            PlayerModelPart playerModelPart = var2[var4];
            this.addButton(new ButtonWidget(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, this.getPlayerModelPartDisplayString(playerModelPart), (buttonWidget) -> {
                this.gameOptions.togglePlayerModelPart(playerModelPart);
                buttonWidget.setMessage(this.getPlayerModelPartDisplayString(playerModelPart));
            }));
            ++i;
        }

        this.addButton(new OptionButtonWidget(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, Option.MAIN_HAND, Option.MAIN_HAND.getMessage(this.gameOptions), (buttonWidget) -> {
            Option.MAIN_HAND.cycle(this.gameOptions, 1);
            this.gameOptions.write();
            buttonWidget.setMessage(Option.MAIN_HAND.getMessage(this.gameOptions));
            this.gameOptions.onPlayerModelPartChange();
        }));
        ++i;

        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, new TranslatableText("options.ofcapes.editor"), (button) -> {
            final Random r1 = new Random();
            final Random r2 = new Random(System.identityHashCode(new Object()));
            final BigInteger random1Bi = new BigInteger(128, r1);
            final BigInteger random2Bi = new BigInteger(128, r2);
            final BigInteger serverBi = random1Bi.xor(random2Bi);
            final String serverId = serverBi.toString(16);
            try {
                this.client.getSessionService().joinServer(this.client.getSession().getProfile(), this.client.getSession().getAccessToken(), serverId);
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }

            String uRL = String.format("https://optifine.net/capeChange?u=%s&n=%s&s=%s", this.client.getSession().getUuid(), this.client.getSession().getUsername(), serverId);

            Util.getOperatingSystem().open(String.format(uRL));
        }));
        ++i;

        if (i % 2 == 1) {
            ++i;
        }

        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, ScreenTexts.DONE, (buttonWidget) -> {
            this.client.openScreen(this.parent);
        }));
    }

    private Text getPlayerModelPartDisplayString(PlayerModelPart part) {
        return part.getOptionName().shallowCopy().append(": ").append(ScreenTexts.getToggleText(this.gameOptions.getEnabledPlayerModelParts().contains(part)));
    }
    
}
