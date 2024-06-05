package az.edu.orient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActiveStatusConstant {
    ACTIVE((byte) 1),
    FROZEN((byte) 2),
    INACTIVE((byte) 0);

    private final byte value;
}
