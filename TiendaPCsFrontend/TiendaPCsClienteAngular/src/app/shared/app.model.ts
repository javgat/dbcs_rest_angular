/*export interface Vino {
    bodega: String,
    categoria: String,
    comentario: String,
    denominacion: String,
    id: Number,
    nombrecomercial: String
}*/
export interface EmpleadoLogin{
    nif: String,
    password: String
}

export interface Empleado{
    nif: String,
    pais: String
}

export interface Configuracionpc{
    idconfiguracion : Number,
    tipocpu : String,
    velocidadcpu : Number,
    capacidadram : Number,
    capacidaddd : Number,
    velocidadtarjetagrafica : Number,
    memoriatarjetagrafica : Number,
    precio: Number
}

export interface Currency{
    code: String,
    name: String,
    symbol: String
}

export interface ArrayCurrency{
    currencies : Currency[]
}