package net.bnijik.schooldbcli.dto;

import java.util.Objects;

public record GroupDto(long groupId, String groupName) {
    public GroupDto {
        Objects.requireNonNull(groupName);
    }

    @Override
    public String toString() {
        return "{ "
                + "\"groupId\": " + groupId + ", "
                + "\"groupName\": \"" + groupName + "\""
                + " }";
    }
}
