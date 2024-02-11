package net.bnijik.schooldbcli.dao;

import java.util.Objects;

public final class Page {
    public static final String PAGE_LIMIT_PARAM = "limit";
    public static final String PAGE_OFFSET_PARAM = "offset";
    private final long page;
    private final long size;

    public Page(long page, long size) {
        this.page = page;
        this.size = size;
    }

    public static Page of(long page, long size) {
        if (page <= 0) {
            throw new IllegalArgumentException("Page cannot be less than zero");
        } else if (size <= 0) {
            throw new IllegalArgumentException("Size cannot be less than zero");
        }
        return new Page(page, size);
    }

    public long getLimit() {
        return size;
    }

    public long getOffset() {
        return (page - 1) * size;
    }

    public Page next() {
        return Page.of(page + 1, size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page page1)) return false;
        return page == page1.page && size == page1.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size);
    }

    @Override
    public String toString() {
        return "Page{" + "page=" + page + ", size=" + size + '}';
    }

}
