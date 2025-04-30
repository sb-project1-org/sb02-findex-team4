package com.sprint.findex.sb02findexteam4.config;

import org.mapstruct.Builder;
import org.mapstruct.MapperConfig;

@MapperConfig(
    componentModel = "spring",
    builder = @Builder(disableBuilder = false)
)
public class GlobalMapperConfig {

}
