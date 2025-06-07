package com.funcation.screen;

import com.funcation.screen.TradeBlockMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiGraphics;

/**
 * TradeBlockScreen - Screen class for rendering the Trade Block UI.
 * Handles drawing the background, slots, and title.
 * @since 1.0.0
 */
public class TradeBlockScreen extends AbstractContainerScreen<TradeBlockMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("funcation", "textures/gui/trade_block.png");

    public TradeBlockScreen(TradeBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        // Draw slot highlights for input/output
        int slotSize = 18;
        guiGraphics.fill(x + 44 - 1, y + 35 - 1, x + 44 + slotSize, y + 35 + slotSize, 0x30FFFFFF); // Input 1
        guiGraphics.fill(x + 62 - 1, y + 35 - 1, x + 62 + slotSize, y + 35 + slotSize, 0x30FFFFFF); // Input 2
        guiGraphics.fill(x + 120 - 1, y + 35 - 1, x + 120 + slotSize, y + 35 + slotSize, 0x30FFEEAA); // Output
        // Visual indicator for locked trades (example: red X over output slot if locked)
        if (!menu.stillValid(this.minecraft.player)) {
            int cx = x + 120 + slotSize / 2;
            int cy = y + 35 + slotSize / 2;
            int r = slotSize / 2 - 2;
            guiGraphics.fill(cx - r, cy - 1, cx + r, cy + 1, 0xB0FF0000); // horizontal
            guiGraphics.fill(cx - 1, cy - r, cx + 1, cy + r, 0xB0FF0000); // vertical
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Move title slightly right and make it white
        guiGraphics.drawString(this.font, this.title, 14, 6, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0x404040, false);
        // Tier progress indicator
        int uniqueCompleted = menu.getUniqueTradesCompletedForCurrentTier();
        int uniqueRequired = menu.getUniqueTradesRequiredForCurrentTier();
        int currentTier = menu.getCurrentTier();
        int progressX = 16; // Further left
        int progressY = 20; // Just below the title
        int progressColor = 0xFFFFFF; // White
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.85f, 0.85f, 1.0f); // Reduce font size for progress
        String progress = "Tier " + currentTier + " Progress:";
        int textWidth = this.font.width(progress);
        guiGraphics.drawString(this.font, progress, (int)(progressX / 0.85f), (int)(progressY / 0.85f), progressColor, false);
        guiGraphics.pose().popPose();
        // Draw progress bar to the right of the text, on the same line, but moved up 10px and left 20px
        int barWidth = 60; // 2/3 of previous 90px width
        int barHeight = 7;
        int barX = (int)(progressX / 0.85f) + textWidth - 10; // 8px padding - 20px left
        int barY = (int)(progressY / 0.85f) - 4; // +2px for centering, -10px up
        float percent = uniqueRequired > 0 ? (float) uniqueCompleted / uniqueRequired : 0f;
        int filled = (int) (barWidth * Math.min(percent, 1.0f));
        // Draw light grey border
        int borderColor = 0xFFCCCCCC; // Light grey
        guiGraphics.fill(barX - 1, barY - 1, barX + barWidth + 1, barY, borderColor); // Top
        guiGraphics.fill(barX - 1, barY + barHeight, barX + barWidth + 1, barY + barHeight + 1, borderColor); // Bottom
        guiGraphics.fill(barX - 1, barY, barX, barY + barHeight, borderColor); // Left
        guiGraphics.fill(barX + barWidth, barY, barX + barWidth + 1, barY + barHeight, borderColor); // Right
        // Draw background and filled portion
        guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF333333); // Background
        guiGraphics.fill(barX, barY, barX + filled, barY + barHeight, 0xFF00FF00); // Filled portion
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        // Tooltips for input/output slots
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int slotSize = 18;
        int tooltipOffsetY = 18; // Move tooltip below slot to avoid overlap
        if (isHovering(x + 44, y + 35, slotSize, slotSize, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font, Component.literal("Input 1: Trade item"), mouseX, mouseY + tooltipOffsetY);
        } else if (isHovering(x + 62, y + 35, slotSize, slotSize, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font, Component.literal("Input 2: (Optional)"), mouseX, mouseY + tooltipOffsetY);
        } else if (isHovering(x + 120, y + 35, slotSize, slotSize, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font, Component.literal("Output: Collect result"), mouseX, mouseY + tooltipOffsetY);
        }
    }

    private boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
