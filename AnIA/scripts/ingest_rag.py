import asyncio

from app.rag import index_knowledge


async def main() -> None:
    result = await index_knowledge()
    print(f"Archivos indexados: {result['files']}")
    print(f"Fragmentos indexados: {result['chunks']}")


if __name__ == "__main__":
    asyncio.run(main())
