export interface EmpleadoLogin{
    nif: String,
    password: String
}

export class Empleado{
    readonly nif: String;
    readonly pais: String;
    constructor(nif? : String, pais? : String){
        this.nif = nif || "";
        this.pais = pais || "";
    }

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

export interface MensajeLogin{
    mensaje : String
}

export enum Tipo{
    SUCCESS = "success",
    ERROR = "danger",
    WARNING= "warning",
    INFO="info"
}

export class Mensaje{
    readonly texto: String;
    readonly type : Tipo;
    readonly mostrar : boolean;
    constructor(mensaje?:String, type?:Tipo, mostrar?:boolean){
        this.texto = mensaje || "";
        this.type = type || Tipo.INFO;
        this.mostrar = mostrar || false;
    }
}