package com.hecosw.trailtracker.data.model.mapper

fun interface DataToDomainModelMapper<DataModel, DomainModel> {
    fun map(dataModel: DataModel): DomainModel
}
