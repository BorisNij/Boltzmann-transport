package net.bnijik.schooldbcli.model;

import java.util.Objects;

public record Group(long groupId, String groupName) {
    public Group {
        Objects.requireNonNull(groupName);
    }
}
