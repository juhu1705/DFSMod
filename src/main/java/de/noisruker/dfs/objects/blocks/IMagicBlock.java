package de.noisruker.dfs.objects.blocks;

/**
 * Implement this interface to a block to make it magic.
 *
 * @author Juhu1705
 * @Category Blocks
 */
public interface IMagicBlock {

    /**
     * Sets this blocks power to the given power.
     * If the given power is larger then this blocks {@link IMagicBlock:getMaxPower() max power} the
     * power is set to this blocks max power.
     * @param power The given power
     */
    public void setPower(float power);

    /**
     * @return The actual power of this block
     */
    public float getPower();

    /**
     * Sets the max power of this block to the given power
     * @param power The given power
     */
    public void setMaxPower(float power);

    /**
     * @return The actual max power of this block
     */
    public float getMaxPower();

    /**
     * Adds the named value of power to the Block
     *
     * @param power The power to add
     * @return Rest of the power, if this blocks power is full, else 0
     */
    public float addPower(float power);

    /**
     * Reduce the power by the specific amount. It does only work when enough power is stored in this block
     *
     * @param power Power to reduce
     * @return True, if this amount of power could be reduced.
     */
    public boolean usePower(float power);

}
