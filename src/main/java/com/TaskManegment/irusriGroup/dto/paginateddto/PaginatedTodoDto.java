package com.TaskManegment.irusriGroup.dto.paginateddto;

import com.TaskManegment.irusriGroup.dto.responsedto.ResponseTodoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PaginatedTodoDto {
    private List<ResponseTodoDto> list;
    private long dataCount;
}
