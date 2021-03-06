package helper;

import java.awt.*;

/**
 * This is class which extends GridBagContraints. You can use it instead GridBagConstraints.
 * It has few constructors with parameters with names the same like GridBagConstraints fields/methods.
 */

public class GBC extends GridBagConstraints {

    public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }

    public GBC setAnchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GBC setFill(int fill) {
        this.fill = fill;
        return this;
    }

    public GBC setWeight(double weightx, double weighty) {
        this.weightx = weightx;
        this.weighty = weighty;
        return this;
    }

    public GBC setInsets(int x) {
        this.insets = new Insets(x, x, x, x);
        return this;
    }

    public GBC setInsets(int a, int b, int c, int d) {
        this.insets = new Insets(a, b, c, d);
        return this;
    }

}