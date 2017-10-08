package gs.controller.mapper;

import gs.controller.resource.InmateResource;
import gs.model.Inmate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InmateMapper {

    InmateMapper INSTANCE = Mappers.getMapper(InmateMapper.class);

    @Mapping(source = "id", target = "inmateId")
    InmateResource map(Inmate source);

    @Mapping(source = "inmateId", target = "id")
    Inmate map(InmateResource source);

}
