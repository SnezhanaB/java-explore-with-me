package ru.practicum.ewm.utils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ChunkRequest implements Pageable {

    private final int offset;
    private final int limit;

    // this attribute can be let out if you don't need it
    private Sort sort = Sort.by(Sort.Direction.ASC, "id");

    public ChunkRequest(int offset, int limit, Sort sort) {
        if (offset < 0)
            throw new IllegalArgumentException("Offset must not be less than zero!");

        if (limit < 1)
            throw new IllegalArgumentException("Limit must not be less than 1!");

        this.offset = offset;
        this.limit = limit;

        if (sort != null) {
            this.sort = sort;
        }
    }

    public ChunkRequest(int offset, int limit) {
        this(offset, limit, null);
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public Pageable next() {
        return new ChunkRequest(this.offset + this.limit, this.limit, this.sort);
    }

    @Override
    public Pageable previousOrFirst() {
        if (this.offset - this.limit < 0) return this.first();
        return new ChunkRequest(this.offset - this.limit, this.limit, this.sort);
    }

    @Override
    public Pageable first() {
        return new ChunkRequest(0, this.limit, this.sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return this.offset - this.limit > 0;
    }
}