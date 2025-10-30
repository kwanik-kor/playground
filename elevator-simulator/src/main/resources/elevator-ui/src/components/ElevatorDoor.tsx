import { motion } from 'motion/react';

interface ElevatorDoorProps {
  isOpen: boolean;
}

export function ElevatorDoor({ isOpen }: ElevatorDoorProps) {
  return (
    <div className="relative w-[360px] h-[500px] overflow-hidden">
      {/* Elevator frame - stainless steel */}
      <div className="absolute inset-0 bg-gradient-to-br from-[#c8c8c8] via-[#b0b0b0] to-[#989898] rounded-sm shadow-2xl">
        {/* Frame border */}
        <div className="absolute inset-0 border-4 border-[#808080]" />
        
        {/* Inner shadow for depth */}
        <div className="absolute inset-0 shadow-inner-strong pointer-events-none" />
        
        {/* Door container - clips doors inside frame */}
        <div className="absolute inset-0 overflow-hidden">
          {/* Left door - brushed stainless steel */}
          <motion.div
            className="absolute top-0 left-0 h-full w-1/2 bg-gradient-to-r from-[#a8a8a8] via-[#c0c0c0] to-[#b0b0b0] steel-door"
            initial={{ x: 0 }}
            animate={{ x: isOpen ? '-100%' : 0 }}
            transition={{ 
              duration: 1.5, 
              ease: [0.645, 0.045, 0.355, 1],
              type: "tween"
            }}
            style={{
              borderRight: '2px solid #707070'
            }}
          >
            {/* Brushed metal texture */}
            <div className="absolute inset-0 brushed-metal" />
            
            {/* Vertical metal panels */}
            <div className="absolute inset-x-0 top-8 bottom-8 mx-4 border-2 border-[#888]" />
            <div className="absolute inset-x-0 top-16 bottom-16 mx-8 border border-[#999]" />
            
            {/* Metallic highlight */}
            <div className="absolute inset-0 bg-gradient-to-br from-white/30 via-transparent to-black/20 pointer-events-none" />
            
            {/* Door edge shadow */}
            <div className="absolute right-0 inset-y-0 w-8 bg-gradient-to-l from-black/20 to-transparent pointer-events-none" />
          </motion.div>

          {/* Right door - brushed stainless steel */}
          <motion.div
            className="absolute top-0 right-0 h-full w-1/2 bg-gradient-to-l from-[#a8a8a8] via-[#c0c0c0] to-[#b0b0b0] steel-door"
            initial={{ x: 0 }}
            animate={{ x: isOpen ? '100%' : 0 }}
            transition={{ 
              duration: 1.5, 
              ease: [0.645, 0.045, 0.355, 1],
              type: "tween"
            }}
            style={{
              borderLeft: '2px solid #707070'
            }}
          >
            {/* Brushed metal texture */}
            <div className="absolute inset-0 brushed-metal" />
            
            {/* Vertical metal panels */}
            <div className="absolute inset-x-0 top-8 bottom-8 mx-4 border-2 border-[#888]" />
            <div className="absolute inset-x-0 top-16 bottom-16 mx-8 border border-[#999]" />
            
            {/* Metallic highlight */}
            <div className="absolute inset-0 bg-gradient-to-bl from-white/30 via-transparent to-black/20 pointer-events-none" />
            
            {/* Door edge shadow */}
            <div className="absolute left-0 inset-y-0 w-8 bg-gradient-to-r from-black/20 to-transparent pointer-events-none" />
          </motion.div>
        </div>
      </div>

      <style>{`
        .steel-door {
          box-shadow: 
            inset 0 0 30px rgba(0, 0, 0, 0.15),
            0 4px 20px rgba(0, 0, 0, 0.3);
        }

        .brushed-metal {
          background-image: repeating-linear-gradient(
            0deg,
            transparent,
            transparent 1px,
            rgba(255, 255, 255, 0.08) 1px,
            rgba(255, 255, 255, 0.08) 2px,
            transparent 2px,
            transparent 3px,
            rgba(0, 0, 0, 0.03) 3px,
            rgba(0, 0, 0, 0.03) 4px
          );
        }

        .shadow-inner-strong {
          box-shadow: inset 0 0 40px rgba(0, 0, 0, 0.4);
        }
      `}</style>
    </div>
  );
}